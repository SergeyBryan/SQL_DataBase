CREATE TABLE faculty
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY
        PRIMARY KEY,
    name  TEXT,
    color TEXT
);
CREATE TABLE student
(
    id         BIGINT generated by default as identity
        PRIMARY KEY,
    name       TEXT,
    age        INTEGER,
    faculty_id BIGINT REFERENCES faculty
);

CREATE TABLE faculty_students
(
    faculty_id bigint NOT NULL REFERENCES faculty,
    student_id bigint NOT NULL UNIQUE REFERENCES student
);


CREATE TABLE avatar
(
    avatar_id  BIGINT NOT NULL PRIMARY KEY,
    data       oid,
    file_path  VARCHAR,
    file_size  BIGINT,
    media_type VARCHAR
);
;