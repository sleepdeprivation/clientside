package groovycarnage.com.hermes.model;

import java.util.ArrayList;


/*

//current schema

create table User 		(
							userID int primary key auto_increment,
							username varchar(32) unique not null,
							password varchar(32) not null
						);

#after reading about GSON, separated into 2 message types for easiness
#while these may be redundant and essentially the same, only OP should have a lat/lon
#and only replies should have a parent
#if this proves to be a problem we can migrate later fairly easily I think
create table HeadMessage	(
							messageID int not null primary key auto_increment,
							posterID int not null references User(userID),
							content varchar(1024),
							lat float(10,6),
							lon float(10,6),
							numUpvotes int default 0,
							numDownvotes int default 0
						);

create table ReplyMessage(
							messageID int not null primary key auto_increment,
							posterID int not null references User(userID),
							parentID int not null references HeadMessage(messageID),
							content varchar(1024),
							numUpvotes int default 0,
							numDownvotes int default 0
						);
*/


public class Thread {

    public Message head;
    public ArrayList<Message> replies;

}
