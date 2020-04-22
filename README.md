# p-manager-springboot
Spring Boot BackEnd for Project Managment Assignment  

Configurations that should be done:

This project is connected with a MySQL database, so before running, a new schema should be created with name testdb (or configure different name in application.properties).

Run the Spring Boot Project. The project is build to use two roles, Admin and User.Execute in the database the following query:

INSERT INTO roles(name) VALUES('ROLE_USER'); INSERT INTO roles(name) VALUES('ROLE_ADMIN');

Create a first admin user by sending the following request body in API_URL/api/auth/signup { "username": "admin", "email": "admin@email.com", "password": "password", "role": "ADMIN"}

You can now login add new Users from the UI, Manage Tasks and view Statistics !

*Optional After creating the first admin user annotate API_URL/api/auth/signup contoller with @PreAuthorize("hasRole('ADMIN')") so no new users can be created without admin authorization
