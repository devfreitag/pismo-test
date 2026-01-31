CREATE TABLE operation_type (
    operation_type_id BIGINT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE account (
    account_id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(255) NOT NULL,
    CONSTRAINT uk_account_document_number UNIQUE (document_number)
);

CREATE TABLE transaction (
    transaction_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    operation_type_id BIGINT NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id)
        REFERENCES account(account_id),
    CONSTRAINT fk_transaction_operation_type FOREIGN KEY (operation_type_id)
        REFERENCES operation_type(operation_type_id)
);
