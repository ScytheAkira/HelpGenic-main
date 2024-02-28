use helpgenic;

create table `User`(
	uid int primary key auto_increment,
	`name` varchar(50),
    email varchar(50) unique,
    `password` varchar(30),
    gender boolean,
    DOB date,
    type varchar(1)
);



create table Patient(

	pid int Unique,
	bloodGroup ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-'),
    phoneNum varchar(11),
    Foreign Key(pid) references `User`(uid),
    Primary key(pid)
)
;




create table Doctor (

	docId int Unique,
	specialization ENUM('Pediatrics','Opthamologist','Dermatologist','Allergist','Neurologist','Pathologist','Urologist','Anesthesiologist'),
	surgeon boolean,
    degree longBLOB,
    accNum varchar(30),
    verified tinyint(1),
    rating float ,
    ratingCount int,
	meetId varchar(100),
    Foreign Key(docId) references `User`(uid),
    Primary key(docId)
);



create table Donor(

	did int Unique,
	bloodGroup ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-'),
    phoneNum varchar(50),
    address varchar(100),
    Foreign Key(did) references `User`(uid),
    Primary key(did)
);



create table comments(
    docId int ,
    pId int,
    pComment varchar(500),
    Foreign key (pId) References Patient(pId),
    Foreign key(docId) References Doctor(docId),
    Primary key(docId , pComment)
);


create table Appointment(

     appId int Primary key auto_increment,
	 docId int ,
     pId int ,
    `date` date,
     stime Time,
     eTime Time,
     foreign key(docId) references Doctor(docId),
     foreign key(pId) references Patient(pId)
     
);


create table Document(

	documentId int primary key auto_increment,
    appId int,         
    documentBinary longBLOB,
    Foreign key(appId) references Appointment(appId)
);

create table Prescripton(

	presId int Unique,
	days int,
    Foreign key(presId) references Appointment(appId),
	Primary key(presId)
    
    
)
;


create table medicineDosage(
     
	`name` varchar(50),
     dosage varchar(30),
     morning boolean,
     night boolean,
	 presId int,
	 afternoon boolean,
	 Foreign key(presId) references Prescripton(presId),
     Primary key(`name`,dosage,presId)
     
);


create table appSchedule(

	aptId int primary key auto_increment,
    `day` ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'),
    fee float,
    `type` ENUM('v' , 'p'),
    docId int,
	sTime time,
    eTime time,
    Foreign key(docId) references Doctor(docid)
    
);


create table phyAppSchedule (

    aptId int Unique,
	clinicName varchar(50),
    assistantPhoneNum varchar(11),
    latts double,
    longs double,
    Foreign key(aptId) references appSchedule(aptId),
    Primary key(aptId)
    
);



create table virtualAppSchedule (
     aptId int Unique,
     Foreign key(aptId) references appSchedule(aptId) on delete cascade ,
     Primary key(aptId)
     
);





/*-------------------------------------- Procedures -----------------------------------------*/
insert into virtualAppSchedule
values
(5)

DELIMITER //
create procedure matchCredentials(IN email varchar(50) , IN pas varchar(30) , OUT userType varchar(1),OUT id int)
begin
   
   set userType = 'N';
   set id = 0;
   select u.type,u.uid INTO userType, id from `User` as u
   where email = u.email and pas = u.`password` ;
   
end //
DELIMITER ;



DELIMITER //
create procedure updateRating(in r float,in did int)
begin
   
   update doctor set rating = (rating+r)/ratingcount+1, ratingcount=ratingcount+1
   where docid=did;
   
end //
DELIMITER ;


DELIMITER //

create PROCEDURE insertComment (in p_id int, IN d_id int, IN p_comment varchar(500))
begin
		INSERT INTO comments  VALUES (d_id, p_id, p_comment);
end //
DELIMITER ;



call matchCredentials('def@gmail.com','Muhammad167',@temp,@id);
select @temp as COL1 , @id as COL2;

SELECT CONVERT_TZ( NOW(), '+00:00', '+05:00') ;

/* ----------------------------------- Views ------------------------------------- */ 
-- for patient

create view upcomingAppointments
as
select ap.pId as pId, ap.appId ,u.gender ,d.meetId,d.docId ,u.`name` , d.specialization,d.surgeon , ap.`date` , ap.sTime , ap.eTime, DAYNAME(`date`) as day, p.sTime as docsTime , p.eTime as doceTime
from
Appointment ap join Doctor d on ap.docId = d.docId join `User` u on d.docId= u.uid join appSchedule p on p.docId = d.docId 
where  p.type='v' and p.day = DAYNAME(`date`) and ( (`date`= CURDATE() and ap.eTime > CONVERT_TZ( NOW(), '+00:00', '+05:00')  ) OR  `date`> CURDATE() ) 
Order By `date` , ap.eTime ;


select * from upcomingAppointments ua where pId = 60;

create view upcomingAppointmentsForDoctor
as
select ap.docId,ap.pid ,u.`name` ,ap.`date` , ap.sTime , ap.eTime from Appointment ap join Patient p on ap.pId = p.pid join `User` u on p.pId= u.uid
where  ( (`date`= CURDATE() and ap.eTime > CONVERT_TZ( NOW(), '+00:00', '+05:00')  ) OR  `date`> CURDATE() )
Order By `date` , ap.eTime ;

select * from upcomingAppointmentsForDoctor ua where docId = 19;



create view getPatientInfo
as
select p.pid, u.`name` ,u.email , p.phoneNum , p.bloodgroup , gender, DATE_FORMAT(FROM_DAYS(DATEDIFF(now(),u.DOB)),  '%Y') as age
from Patient p join `user` u on p.pid = u.uid;


alter view getPreviousAppointmentsInfo
as
select ap.appid ,docId , pid ,ap.`date` from Appointment ap where( (`date`= CURDATE() and ap.sTime <= CONVERT_TZ( NOW(), '+00:00', '+05:00')  ) OR  `date`< CURDATE() )
order by ap.`date` desc, ap.sTime desc;

select * from getPreviousAppointmentsInfo where pid=5 and docId = 19;


create view getPreviousPatients
as
select * from
(
	select ap.docId,ap.pid ,u.`name`,ap.sTime,CONVERT_TZ( NOW(), '+00:00', '+05:00')  from Appointment ap join Patient p on ap.pId = p.pid join `User` u on p.pId= u.uid 
	where  ( (`date`= CURDATE() and ap.sTime <= CONVERT_TZ( NOW(), '+00:00', '+05:00')  ) OR  `date`< CONVERT_TZ(CURDATE(),'+00:00', '+05:00') )
	Order By `date` desc, ap.sTime desc
) as temp;

select DISTINCT * from getPreviousPatients where docId=59;


                
create view getPreviousDoctors
as
select * from
(
	select  ap.docId,ap.pid,u.`name`,u.email,u.gender,d.rating,d.specialization,d.surgeon  from Appointment ap join Doctor d on ap.docId = d.docId join `User` u on d.docId= u.uid 
	where  ( (`date`= CURDATE() and ap.eTime < CONVERT_TZ( NOW(), '+00:00', '+05:00')  ) OR  `date`< CURDATE() ) Order By `date`desc , ap.sTime desc 
    
) as temp;


select DISTINCT * from getPreviousDoctors where pid=56;



