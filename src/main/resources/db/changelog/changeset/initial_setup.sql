CREATE TABLE Usr(
                  id BIGSERIAL PRIMARY KEY,
                  username varchar,
                  age int,
                  password varchar,
                  role varchar
);

CREATE TABLE Home(
  id BIGSERIAL PRIMARY KEY,
  address varchar,
  host_id BIGINT REFERENCES Usr(id)
);


CREATE TABLE Home_Usr (
   usr_id BIGINT REFERENCES Usr(id),
   home_id BIGINT REFERENCES Home(id),
   primary key (usr_id,home_id)
);




