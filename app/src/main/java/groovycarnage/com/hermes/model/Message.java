package groovycarnage.com.hermes.model;

import java.util.ArrayList;


/*

The schema as it is now:

create table User 		(
							userID int primary key auto_increment,
							username varchar(32) unique not null,
							password varchar(32) not null
						);

create table Message	(
							messageID int primary key auto_increment,
							posterID int references User(userID),
							parentID int references Message(messageID),
							content varchar(1024),
							lat float(10,6),
							lon float(10,6),
							numUpvotes int,
							numDownvotes int
						);
*/

/*
    I think I'm going to make the mistake of making this model recursive
 */
public class Message {

    public int messageID;
    public int posterID;
    public int parentID;
    public String content;
    public float lat;
    public float lon;
    public int numUpvotes;
    public int numDownvotes;

    public ArrayList<Message> replies;  //self referencing

}
