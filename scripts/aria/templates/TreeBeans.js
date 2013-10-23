/*
 * Aria Templates 1.4.11 - 15 Oct 2013
 *
 * Copyright 2009-2013 Amadeus s.a.s.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Bean for parsed trees
 */
Aria.beanDefinitions({
    $package : "aria.templates.TreeBeans",
    $description : "Definition of the tree structure generated by the parser when processing a template.",
    $namespaces : {
        "json" : "aria.core.JsonTypes"
    },
    $beans : {
        "Root" : {
            $type : "Statement",
            $description : "Root node of a template tree. It is a special node that does not appear in the template and contains top-level template elements.",
            $mandatory : true,
            $properties : {
                "name" : {
                    $type : "Statement.name",
                    $regExp : /^#ROOT#$/
                },
                "content" : {
                    $type : "Statement.content",
                    $mandatory : true
                },
                "parent" : {
                    $type : "Statement.parent",
                    $description : "Parent element: is null for the root statement.",
                    $mandatory : false
                }
            }
        },

        "Statement" : {
            $type : "json:Object",
            $description : "Template statement. Everything inside a template is represented as a statement in this tree.",
            $properties : {
                "name" : {
                    $type : "json:String",
                    $description : "Name of the statement. It can be one of the special types '#ROOT#', '#TEXT#' and '#EXPRESSION#', or the name of the statement, like 'if' or '@aria:TextField'.",
                    $mandatory : true
                },
                "lineNumber" : {
                    $type : "json:Integer",
                    $description : "Line number corresponding to firstCharParamIndex.",
                    $mandatory : true
                },
                "parent" : {
                    $type : "json:ObjectRef",
                    $description : "Link to the parent statement, or null if it is the root statement.",
                    $mandatory : true
                },
                "content" : {
                    $type : "json:Array",
                    $mandatory : false,
                    $description : "Content of the statement, as an array of statements. It is undefined if the statement was not used as a container. It is always undefined for types 'text ' and 'dollar ', and always defined for type 'root '.",
                    $contentType : {
                        $type : "Statement",
                        $mandatory : true
                    }
                },
                "firstCharContentIndex" : {
                    $type : "json:Integer",
                    $description : "Position of the first character which was parsed into the 'content' field of the statement, in the prepared template string (with original comments and some spaces removed). It is not a mandatory field, so class generation does not rely on it (or only for error reporting).",
                    $minValue : 0,
                    $mandatory : false
                },
                "lastCharContentIndex" : {
                    $type : "json:Integer",
                    $description : "Position of the last character which was parsed into the 'content' field of the statement, in the prepared template string (with original comments and some spaces removed). It is not a mandatory field, so class generation does not rely on it (or only for error reporting).",
                    $minValue : 0,
                    $mandatory : false
                },
                "paramBlock" : {
                    $type : "json:String",
                    $description : "Statement parameter, with new lines at the begining and end removed.",
                    $mandatory : true
                },
                "firstCharParamIndex" : {
                    $type : "json:Integer",
                    $description : "Position of the first character of the statement parameter inside the prepared template string (with original comments and some spaces removed). It is not a mandatory field, so class generation does not rely on it (or only for error reporting).",
                    $minValue : 0,
                    $mandatory : false
                },
                "lastCharParamIndex" : {
                    $type : "json:Integer",
                    $description : "Position of the last character of the statement parameter inside the prepared template string (with original comments and some spaces removed). It is not a mandatory field, so class generation does not rely on it (or only for error reporting).",
                    $minValue : 0,
                    $mandatory : false
                }

            }
        }
    }
});
