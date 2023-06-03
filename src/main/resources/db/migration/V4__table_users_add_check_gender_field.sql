UPDATE users SET gender = 'MALE' where gender NOT IN ('MALE', 'FEMALE');

ALTER TABLE users ADD CONSTRAINT gender_values CHECK (gender IN ('MALE', 'FEMALE'));