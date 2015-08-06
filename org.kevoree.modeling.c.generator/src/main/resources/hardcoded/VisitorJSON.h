#ifndef H_VisitorJSON
#define H_VisitorJSON

#include "Visitor.h"

typedef struct _visitor_json {
  Visitor * visitor;
  void (*destroy)(struct _visitor_json *);
} VisitorJSON;

VisitorJSON * visitor_json_new();

#endif
