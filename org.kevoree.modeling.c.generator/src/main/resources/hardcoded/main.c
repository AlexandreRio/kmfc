#include "DictionaryValue.h"
#include "Dictionary.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
	Dictionary *o = new_Dictionary();

	DictionaryValue *ptr = new_DictionaryValue();

	//poor test
	o->VT->dictionaryAddValues(o, ptr);
	o->VT->delete(o);
	free(o);
	ptr->VT->delete(ptr);
	free(ptr);
}
