package com.bookshelf.res;

public class Openapi {
    public static final String BAD_REQUEST_EXAMPLE =
            """
            {
                "timestamp": "*time of query*",
                "status": "BAD_REQUEST",
                "errors": [
                    "*problem description*"
                ]
            }
            """;
    public static final String OBJECT_NOT_FOUND_EXAMPLE =
            """          
                {
                    "timestamp": "*time of query*",
                    "status": "NOT_FOUND",
                    "errors": "Can't find object by id: {id}"
                }
            """;
    public static final String INCORRECT_DATA =
            """
                {
                    "timestamp": "*time of event*",
                    "status": "INTERNAL_SERVER_ERROR",
                    "errors": [
                        "Incorrect user data"
                    ]
                }
            """;
}
