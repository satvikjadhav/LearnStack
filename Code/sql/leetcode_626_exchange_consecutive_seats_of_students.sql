/*
Input: 
Seat table:
+----+---------+
| id | student |
+----+---------+
| 1  | Abbot   |
| 2  | Doris   |
| 3  | Emerson |
| 4  | Green   |
| 5  | Jeames  |
+----+---------+
Output: 
+----+---------+
| id | student |
+----+---------+
| 1  | Doris   |
| 2  | Abbot   |
| 3  | Green   |
| 4  | Emerson |
| 5  | Jeames  |
+----+---------+
Explanation: 
Note that if the number of students is odd, there is no need to change the last one's seat.
*/

CREATE TABLE seats (
    id INT,
    student VARCHAR(10)
);

INSERT INTO seats VALUES 
(1, 'Amit'),
(2, 'Deepa'),
(3, 'Rohit'),
(4, 'Anjali'),
(5, 'Neha'),
(6, 'Sanjay'),
(7, 'Priya');

-- Method 1
SELECT *,
    CASE 
        WHEN id = (SELECT MAX(id) FROM seats) AND id % 2 = 1 THEN id
        WHEN id % 2 = 0 THEN id - 1
        ELSE id + 1
    END AS new_id
FROM seats;

-- What if the id numbers are not concecutive
-- generate a row number first based on order by id
-- instead of id, we can then use row number

-- Method 2
-- lead and lag function

SELECT *,
    CASE 
        WHEN id = (SELECT MAX(id) FROM seats) AND id % 2 = 1 THEN id -- in this we don't need this
        WHEN id % 2 = 0 THEN COALESCE(LAG(id, 1) OVER (ORDER BY id), id)
        ELSE COALESCE(LEAD(id, 1) OVER (ORDER BY id), id)
    END AS new_id
FROM seats;


-- Updating the id column
-- We will be joining the seats table with the output of the query above on IDs and then update the id with the new_id
UPDATE seats
SET id = new_seats.new_id
FROM (
    SELECT *,
        CASE 
            WHEN id = (SELECT MAX(id) FROM seats) AND id % 2 = 1 THEN id
            WHEN id % 2 = 0 THEN id - 1
            ELSE id + 1
        END AS new_id
    FROM seats;
) AS new_seats
WHERE seats.id = new_seats.id;
