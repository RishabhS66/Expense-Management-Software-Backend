# Expense Management Software [Backend]
```
Coded By: Rishabh Srivastava, Puneet Mangla, Bhanu Ailani, Abhishek Shaw
```
In these times of digitalization, all major organizations are aiming to automate most of their processes for better convenience. One important task for an organization involves filing of expenses by employees for claiming reimbursements. These expenses also need to be approved / rejected by the appropriate authority.

A web application based on **react-redux** at frontend, and **Java** and **PostgreSQL** at backend has been built to provide an automated solution to record and
report business expenses.

This repository includes all the codes for the **backend portion** of [Expense Management System](https://expense-management-system-rs.herokuapp.com/) website. 

The Swagger UI based documentation for API calls can be found [here](https://expense-backend-rs.herokuapp.com/swagger-ui.html#/).

## Database
The database has 6 tables : 
<ul>
  <li> <b>Employees</b> - To store details of employees registered with the organization.
  <li> <b>Clients</b> - To store details of clients working with the organization.
  <li> <b>Projects</b> - To store details of the projects. All projects are associated with a client, and have a team of different employees, with one of employees as the Project Manager.
  <li> <b>Expenses</b> - To store details of the expenses filed by an employee. 
  <li> <b>Documents</b> - To store documents that are submitted while filing expenses by an employee.
  <li> <b>Employee_projects</b> - To store association of projects and the employees that are associated with the project. 
</ul>

## Class Diagram
<div align = "center">
  <kbd>
    <img src = "https://user-images.githubusercontent.com/39689610/134234881-edd20a1f-6d4d-4796-acec-9d2fe5d4621b.png">
  </kbd>
</div>

## Interaction Diagram
<div align = "center">
  <kbd>
    <img src = "https://user-images.githubusercontent.com/39689610/134234976-a64790bb-60dd-45bf-af77-88ab2dbf4674.png">
  </kbd>
</div>

## Notes
1. The **frontend code** for this project can be accessed at this [repo](https://github.com/RishabhS66/Expense-Management-Software-React-App).<br>
2. Both the frontend and backend components of the project have been hosted at [Heroku](https://www.heroku.com/). 
