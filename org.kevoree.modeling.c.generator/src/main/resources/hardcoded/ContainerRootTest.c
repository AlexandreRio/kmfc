#include "ContainerRootTest.h"

#include <stdlib.h>
#include <check.h>

START_TEST (metaClassTest)
{
  Group *g = new_Group();

  ck_assert_str_eq(g->VT->metaClassName(g), "Group");
}
END_TEST

START_TEST (nameTest)
{
  Group *g = new_Group();
  g->name = "test";

  ck_assert_str_eq(g->VT->internalGetKey(g), "test");
}
END_TEST

START_TEST (findByIdTest)
{
  ContainerRoot *r = new_ContainerRoot();
  Group *g = new_Group();
  g->name = "test";
  r->VT->containerRootAddGroups(r, g);
  Group *get = r->VT->containerRootFindgroupsByID(r, "test");

  ck_assert_str_eq(get->VT->internalGetKey(get), g->VT->internalGetKey(g));
}
END_TEST

Suite* containerroot_suite(void)
{
  Suite *s;
  TCase *tc_core;

  s = suite_create("ContainerRoot");

  /* Core test case */
  tc_core = tcase_create("Core");

  tcase_add_test(tc_core, metaClassTest);
  tcase_add_test(tc_core, nameTest);
  tcase_add_test(tc_core, findByIdTest);
  suite_add_tcase(s, tc_core);

  return s;
}
