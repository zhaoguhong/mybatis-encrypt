CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `password` varchar(255) NOT NULL,
                        `user_name` varchar(255) NOT NULL,
                        `age` int(2) DEFAULT NULL,
                        PRIMARY KEY (`id`)
);
