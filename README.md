# library-restapi
Documentation how to use REST endpoints:

1. Add reader:
  @POST localhost:8080/readers
  {
    "firstName": "John",
    "lastName": "Smith",
    "created": "2021-09-30T12:20:00"
  }
  
2. Add book:
  @POST localhost:8080/books
  {
    "title": "The Big Fisherman",
    "author": "Lloyd C. Douglas",
    "yearOfPublication": 1948
  }
  
3. Add copy:
  @POST localhost:8080/copies
  {
    "bookId": 1,
    "status": "AVAILABLE"
  }
  
4. Change status of copy:
  @PATCH localhost:8080/copy/status/1 <- {id}
  {
    "status": "RENTED"
  }
  
5. Check amount of available copies for given book:
  @GET localhost:8080/copies?id=1 <- {bookId}
  
6. Rent a book: 
  @POST localhost:8080/rentals
  {
    "copyId": 1,
    "readerId": 1
  }
  
7. Return book:
  @POST localhost:8080/rentals/complete/1 <- {id}
 
