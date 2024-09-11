## **ACID**

**ACID** is a set of properties that guarantee database transactions are processed reliably. It is crucial for maintaining the integrity of a relational database. Here’s a detailed look at each component:

1. **Atomicity**:
   - **Definition**: Atomicity means that a transaction is an indivisible unit of work. This implies that either all operations of the transaction are executed or none are. It ensures that partial transactions do not leave the database in an inconsistent state.
   - **Example**: In a banking application, if you transfer money from Account A to Account B, atomicity ensures that either both the debit from Account A and the credit to Account B occur, or neither occurs if an error happens.

2. **Consistency**:
   - **Definition**: Consistency ensures that a transaction brings the database from one valid state to another, maintaining all predefined rules, constraints, and data integrity.
   - **Example**: Suppose a database rule enforces that the total amount of money in all accounts must be the same before and after any transaction. Consistency ensures that this rule is upheld throughout.

3. **Isolation**:
   - **Definition**: Isolation ensures that concurrent transactions do not interfere with each other. The intermediate state of a transaction is invisible to other transactions until the transaction is complete.
   - **Example**: If two transactions are running simultaneously, isolation ensures that one transaction’s changes are not visible to the other until it is committed.

4. **Durability**:
   - **Definition**: Durability guarantees that once a transaction has been committed, its changes are permanent, even in the event of a system crash or failure.
   - **Example**: After a bank transfer transaction is completed, the changes will persist even if there is a sudden power outage.

