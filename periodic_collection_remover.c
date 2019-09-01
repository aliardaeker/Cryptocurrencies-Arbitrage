/*
	* Script for deleting old documents which will not be needed by Servlet to analyze.
* Cron job call this periodically.
* It's using mongo C driver
* Code has some code snippets from http://mongoc.org/libmongoc/current/tutorial.html#creating-bson-documents
*
*/

#include <mongoc.h>
#include <bson.h>
#include <stdio.h>
#include <time.h>

int main (int argc, char *argv[])
{
    mongoc_client_t *mongo_client;
    mongoc_collection_t *collection;

    // bson_t     *document;

    mongoc_init ();

    // document = bson_new ();

    // client initialization.
    mongo_client = mongoc_client_new ("mongodb://localhost:27017");

    // set db and collection to remove from.
    collection = mongoc_client_get_collection (mongo_client, "exchangedb_2", "exchanges");


 	time_t t = time(NULL);
  	struct tm tm = *localtime(&t);

   	struct tm current_date = { 0 };

	current_date.tm_year = tm.tm_year;
	current_date.tm_mon = tm.tm_mon;
	current_date.tm_mday = tm.tm_mday-1;


   printf("%d %d %d ",current_date.tm_year,current_date.tm_mon,current_date.tm_mday);
   
   bson_t *query;
   query = BCON_NEW ("date", "{", "$lt", BCON_DATE_TIME (mktime (&current_date) * 1000), "}");
    mongoc_collection_remove (collection, MONGOC_REMOVE_NONE, query, NULL,NULL);

    //cleanup driver 
    mongoc_cleanup ();
}