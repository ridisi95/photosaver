use `photosaver`;

drop table pictures;
drop table images;

CREATE TABLE `images` (
`id` int(11) NOT NUll primary key auto_increment,
`image_link` VARCHAR(255) ,
#`image` BLOB
`image` MEDIUMBLOB );

CREATE TABLE `pictures` (
   `id` VARCHAR(255) NOT NULL,
   `cropped_picture_id` int(11),
   `full_picture_id` int(11),
   `author` varchar(255),
   `camera` varchar(255),
   `tags` varchar(255),
   PRIMARY KEY (`id`),
   foreign key (`cropped_picture_id`) references images(id),
   foreign key (`full_picture_id`) references images(id) )
   ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1