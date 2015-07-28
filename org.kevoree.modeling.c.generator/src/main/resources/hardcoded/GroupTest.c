#include "GroupTest.h"

#include <stdlib.h>
#include <check.h>

START_TEST (metaClassTest)
{
  Group *g = new_Group();

  ck_assert_str_eq(g->VT->metaClassName(g), "Group");
}
END_TEST

Suite* group_suite(void)
{
  Suite *s;
  TCase *tc_core;

  s = suite_create("Group");

  /* Core test case */
  tc_core = tcase_create("Core");

  tcase_add_test(tc_core, metaClassTest);
  suite_add_tcase(s, tc_core);

  return s;
}
