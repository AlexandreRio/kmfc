#include "ContainerRoot.h"
#include "Group.h"

#include <stdlib.h>
#include <check.h>

START_TEST (test1)
{
  ContainerRoot *r = new_ContainerRoot();
  Group *g = new_Group();
  printf("%s id: %s key: %s\n", r->VT->metaClassName(r), r->generated_KMF_ID, r->VT->internalGetKey(r));
  g->name = "test";
  printf("%s key: %s\n", g->VT->metaClassName(g), g->VT->internalGetKey(g));
  r->VT->containerRootAddGroups(r, g);
  Group *get = r->VT->containerRootFindgroupsByID(r, "test");
  printf("%s key: %s\n", get->VT->metaClassName(get), get->VT->internalGetKey(get));
  ck_assert_str_eq(get->VT->metaClassName(get), "test");
}
END_TEST

Suite * money_suite(void)
{
  Suite *s;
  TCase *tc_core;

  s = suite_create("Money");

  /* Core test case */
  tc_core = tcase_create("Core");

  tcase_add_test(tc_core, test1);
  suite_add_tcase(s, tc_core);

  return s;
}

int main(void)
{

  int number_failed;
  Suite *s;
  SRunner *sr;

  s = money_suite();
  sr = srunner_create(s);

  srunner_run_all(sr, CK_NORMAL);
  number_failed = srunner_ntests_failed(sr);
  srunner_free(sr);
  return (number_failed == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}
