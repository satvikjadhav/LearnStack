# [Data Modeling](https://www.youtube.com/watch?v=OjVZdT8XAds)
	Diagramming the data objects/points in an information system
	Any type of data modeling will be always linked to our business needs and requirements, and how easy we want the information to be shown to the end users
	Data modeling is now about the how the data is going to be processed, but how the the data is best represented
	Thinking about how we are going to be storing the data, segregate it, build the logic for the data to be fetched

## Transactional/Operational Databases
	Relational Data Modeling
When we are concerned with **transactional databases** we are not looking into any analytical parts, and we are not trying to get anything out of this information which is not going to be operational. 

For these types of database designs we will be looking at two different types of models; **relational model** and **noSQL**. There is not much logic to be done, we won't be doing many joins for the information here. At most we would be displaying only some information towards our application.

**Relational data models** prioritize the maintenance of data integrity. This practice ensures data protection and consistency which are critical aspects of data model design, its implementation, and its future usage for storing, processing, and retrieving data.

Relational data models store present data. Their primary purpose is to model relational databases, which are especially useful to establishing and managing an overview of current data.

So for this we are looking at our regular **entity-relationship diagram** (ERD)
The advantage of doing this is that we can get our information fast.

The connections between tables in relational databases are made by relational references using primary and foreign keys
There are three types of keys in a relational data model:
- **Primary**: A primary key identifies a particular row in a database table.
- **Foreign**: A foreign key refers to the primary key of another table.
- **Candidate**: A candidate key can be selected and used as the primary key.

It is much cheaper to go for the **noSQL** solution than a SQL one. Another advantage of **noSQL** over plain SQL is that we can get our data very quickly and cheaply from our databases. 
- **GPS application example**
	- We are keeping information about the GPS coordinates of moving vehicles
	- We will be getting lots of information such as GPS coordinates very quickly
	- And we want to be displaying this information
	- In this case, a noSQL database like MongoDB will allow us to fetch this information very quickly and showcase this information in our application

### Normalization
Another essential step of building relational data models is normalization. **Normalization** is a process of analyzing relation schemas based on functional dependencies and relational references in order to decrease redundancy and avoid anomalies. There are several normal forms (NF) but the first three are the most common:
- **1NF (Normal Form)**: Also known as **Atomicity**. Relation is in 1NF if the domain of each attribute contains atomic values. For example, if we have address information of a customer we would split it into separate columns such as city, state, and country columns instead of having the entire address in on column
- **2NF**:  is based on the concept of full functional dependency. The table is in 2NF it is in 1NF and each non-key attribute must depend on the entire primary or candidate key based on duplicity elimination in the current relation. For example, there is a relation related to students and it not only stores information about each student, but also contains information about school (e.g., faculty name, address, or contact information), which is not related to students. In this situation, it is mandatory to clarify which attributes relate to students versus school, and then accordingly divide one table into two separate tables. The goal of this normal form is to have each table contain information (non-key attributes) that only depend on one primary key.  
- **3NF**: Relation is in 3NF if it is in 2NF and does not have a transitive dependency. Meaning, if attribute X depends on attribute Y, and attribute Y depends on attribute Z, then attribute X should not depend on attribute Z. If this situation exists, splitting the table into at least two individual tables may be a good solution.

By ***normalizing*** our data, we can maintain the ***integrity*** and ***accuracy*** of tables in your data/database model. **Accuracy** eliminates the possibility of data duplication by connecting relations with primary and foreign keys. **Integrity** helps to ensure reliability between relations (to avoid imperfect and isolated records) as well as simplicity, stability, and precision of the data.

This model is highly secure. For example, we can limit users' access by enabling them to interact with only certain tables that are relevant to their work


**Data modeling with noSQL**:
1. Consider the business needs; do we need the information fast, do we need to join the information. 

## Data Lake
	File system storage
When we are looking at a Data Lake it can be either two things; **data lake** or a **data swamp**
**Data Swamp**: all of our information is there in the file storage solution (such as AWS S3 or GCS). It will not be reliable to use as its hard to navigate around here for the information we need. 

In this case, the data model for data lake will be a structure of a file systems so that we can store and fetch our files in an organized manner.

Now the main question arises, how are we going to organize it so people can get the information as easily as possible. 
```
One word for this, which is usually easily said but hardly done, which is intuition
```
In here we want to model our file and directories in a way that is intuitive to the end user; they are able to navigate the folders and files by names. 
So here the names are one of the important parts that should give the user an idea of what the folder and its contents are about.

For example, an engineer gets information form a source system for the past 2 years and they understand how the file system works.
Now there a new person who will be helping this engineer, and the company also now has new sources of data. 
So now they start pulling the data, connecting the data, and storing it in the data lake. 
But this new person does not understand the engineer's current file system for the source data, so the new person ends up creating new files and directories for information that engineer has already mapped out. 
This new information was meant to go together and to be processed together but now they are in two different parts. 

One way to avoid this is by asking ourselves **"If I was a kid would I understand what I am doing right now?"**

This is very abstract. There is no sure way of storing information or a set method/guide way of doing things in a Data Lake. So we should always ask for advice, try to think of it from an outside person's perspective to make it as intuitive and easy to navigate as possible. 

We essentially need to ensure that we can make logic out of how we are storing our data. We need this so that in future this can be followed and be maintained. The main idea here is to have a set order/rule/logic. 

### Data Lake-house
	This is pretty much a combination between a data lake and data warehouse

We store our information as an object such as parquet files. 
We have multiple steps here:
1. We can store our information as json, csv, text files, images, videos, and audios.
2. Next, we have the abstraction layer in which we can store the same information as parquets or any readable data structure. 
3. We should have a platform that can understand both types of data; object storage. And use this information as a star model or a snowflake model, and have some analytical logic to the data for example. 

## Data Warehousing
	Data model that should feed and be best for analytical purposes
	Here we need to have a more robust data modeling process and structure
	Dimensional Data Model is the most used type of modeling technique here

This is where ALL the data from all our business information systems is going to be stored. 

There is also the **analytical layer**. This is where most teams and analysts will be working from. ***So we need to think about how we are going to set or model the information here for it to be more analytical***.

Here we can also go for **Entity-Relationship Model** and **Relational Model** but it is not the best as we would need a lot of abstraction and also think about **scaling up** of our information layer. 

The preferred models to be used in here are **Star Model** and **Snowflake Model** and they are part of the **Dimensional Data Model**. 

### Dimensional Data Model
A dimensional data model is a type of database used for data warehousing and online analytical processing and provides users with denormalized structures for accessing data from a data warehouse

A dimensional data model consists of two types of tables: fact tables and dimensional tables.

We can build your dimensional data model based on different schemas: star, snowflake, or galaxy. In the center of every star schema is a fact table containing measures and foreign keys of connected dimensions. 

A **galaxy schema** is similar to star and snowflake scheme but different in the sense that it has more than one fact table. It usually contains at least two fact tables from two separated dimensional models which share the same dimensional table.

To design dimensional data models, denormalization is the best approach. 

#### Denormalization
**Denormalization** is a process which is usually applied on top of a normalized database/data model. It is done by adding data duplicates or grouping data. 
Denormalization is necessary to increase performance and support scalability due to the fact that this data model deals with a large number of read operations/queries for analytics purposes.

With dimensional data models, performance is increased and response time is decreased due to denormalization and fewer joins between relations in comparison to relational data models. Similar data is grouped in one dimension

### Star Data Model
	Need to ensure that there is only one level of depth from the dimension to the fact
	One level of depth means that we can build our fact tables with only one layer of join to the dimension table
This model is based on **Dimensional Modeling**.
Here we have a **Fact Table**; which has information regarding a business process, measurements. We can think of this as verbs.
Fact table also acts a central table in which we are going to converge information that exists within the dimension tables. 
And a **Dimension Table**; which has descriptive information about the information in the Fact Table. We can think of this as nouns. 
Dimension tables are going to be very big. They are going contain all the information related to one of the attributes/columns of the fact table. 
For example, the address column in a fact table. In the fact table we would have an address key that references to the address dimension table which contains all the address details. 

### Snowflake Data Model
	
The Snowflake model is a sub type of the Star Model.
This model will also contain a Fact table and a Dimension table. However, the dimension tables here will have other dimension tables as well. 

This is a more normalized version of the Star Model. Not necessarily to go all the way to 3NF but we can go to 2NF. Or just to make sure that we can abstract the information and make it more scalable because all new information and dimensions that we need to make can be made from the present dimensions. 

One of the disadvantages of this model is that we will not have one level of depth for joins as we have dimensions that need to be joined on to other dimensions in order to describe or complete our fact table. 
As a result, there will be a higher join complexity here but it will make joins more readable, easy to understand, and easier to build the right information without having to work with massive tables. 


### Graph Data Model
	A Graph Model is higher level model or in order words a more conceptual model
	A very specific model for machine learning usage

This model is mainly used when we have a machine learning engineer or a data scientist because this model is used more in training model. 

In the graph data model we are not aiming to make it so there are individual tables but now we are going to have connections between these tables. We are essentially trying to describe the relationship between the data (adding description to the relation itself). 
For example, Table A is going to be describing Table B, but Table C contains Table B. And Table V is an attribute of Table E. As a result we have more sense done into this information which is not really useful for analytical purposes. 
