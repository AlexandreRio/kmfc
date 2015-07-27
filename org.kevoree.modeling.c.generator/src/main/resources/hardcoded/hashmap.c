/*
 * Generic map implementation.
 */
#include "hashmap.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define INITIAL_SIZE (0)

/*
 * Return an empty hashmap, or NULL on failure.
 */
map_t hashmap_new(PFgetKey getKey) {
	hashmap_map* m = (hashmap_map*) malloc(sizeof(hashmap_map));
	if(!m) goto err;
	
	m->getKey = getKey;

	m->data = (hashmap_element*) calloc(INITIAL_SIZE, sizeof(hashmap_element));
	if(!m->data) goto err;

	m->table_size = INITIAL_SIZE;

	return m;
	err:
		if (m)
			hashmap_free(m);
		return NULL;
}

static int
hashmap_find(hashmap_map* m, const char* key)
{
	int i = 0;
	
	while (i < (m->table_size)) {
		if (strcmp(m->getKey(m->data[i].data), key) == 0)
			return i;
		i++;
	}
	
	return -1;
}

/*
 * Add a pointer to the hashmap with some key
 */
int hashmap_put(map_t in, char* key, any_t value){
	int index;
	hashmap_map* m;

	/* Cast the hashmap */
	m = (hashmap_map *) in;

	/* Find a place to put our value */
	index = hashmap_find(m, key);
	if (index == -1) {
		
		hashmap_element* elems = (hashmap_element*) malloc((m->table_size+1)*sizeof(hashmap_element));
		memcpy(elems, m->data, sizeof(hashmap_element)*m->table_size);
		m->table_size ++;
		free(m->data);
		m->data = elems;
		
		m->data[m->table_size-1].data = value;
	}
	else {
		m->data[index].data = value;
	}

	return MAP_OK;
}

/*
 * Get your pointer out of the hashmap with a key
 */
int hashmap_get(map_t in, char* key, any_t *arg){
	int curr;
	hashmap_map* m;

	/* Cast the hashmap */
	m = (hashmap_map *) in;

	/* Find data location */
	curr = hashmap_find(m, key);
	
	if (curr != -1)
	{
		*arg = (m->data[curr].data);
        return MAP_OK;
	}
	
	/* Not found */
	*arg = NULL;
	return MAP_MISSING;
}

/*
 * Iterate the function parameter over each element in the hashmap.  The
 * additional any_t argument is passed to the function as its first
 * argument and the hashmap element is the second.
 */
int hashmap_iterate(map_t in, PFany f, any_t item) {
	int i;

	/* Cast the hashmap */
	hashmap_map* m = (hashmap_map*) in;

	/* On empty hashmap, return immediately */
	if (hashmap_length(m) <= 0)
		return MAP_MISSING;	

	for(i = 0; i< m->table_size; i++) {
		any_t data = (any_t) (m->data[i].data);
		int status = f(item, data);
		if (status != MAP_OK) {
			return status;
		}
	}

    return MAP_OK;
}

/*
 * Remove an element with that key from the map
 */
int hashmap_remove(map_t in, char* key){
	int i;
	int curr;
	hashmap_map* m;

	/* Cast the hashmap */
	m = (hashmap_map *) in;

	/* Find key */
	curr = hashmap_find(m, key);
	
	if (curr == -1)
	{
		/* Data not found */
		return MAP_MISSING;
	}
	else {
		for(i = curr; i<m->table_size-1; i++){
			m->data[i] = m->data[i+1];
		}
		m->table_size--;
		hashmap_element* tmp = (hashmap_element*) malloc(sizeof(hashmap_element)*m->table_size);
		memcpy(tmp, m->data, sizeof(hashmap_element)*m->table_size);
		free(m->data);
		m->data = tmp;
		return MAP_OK;
	}	
}

/* Deallocate the hashmap */
void hashmap_free(map_t in){
	hashmap_map* m = (hashmap_map*) in;
	free(m->data);
	free(m);
}

/* Return the length of the hashmap */
int hashmap_length(map_t in){
	hashmap_map* m = (hashmap_map *) in;
	if(m != NULL) return m->table_size;
	else return 0;
}
