CREATE MEMORY TABLE IF NOT EXISTS PAYMENTS (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  AMOUNT INT NOT NULL,
  PAYMENTDATE DATE NOT NULL,
  TYPE VARCHAR(100) NULL,
  PAYMENTFORM INT NOT NULL,
  PAYMENTTYPE INT NOT NULL,
  APPLICATIONID INT NOT NULL REFERENCES APPLICATIONS (ID) ON DELETE RESTRICT
);

ALTER TABLE PAYMENTS
ADD FOREIGN KEY (APPLICATIONID)
REFERENCES APPLICATIONS (ID)
ON DELETE CASCADE;
   