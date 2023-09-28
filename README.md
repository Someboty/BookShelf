<h1 align="center">BookShop</h1>
<h3 align="center">Your Online Book Store – Java Spring Project</h3>

Welcome to the BookShop project! This README will guide you through the setup, structure, and functionality of my Java Spring-based online bookstore application. My aim is to create a seamless platform for users to browse, purchase, and manage books, while administrators maintain control over inventory and orders.


## Table of Contents
* [1. Project Overview <a name="project-overview"></a>](#1-project-overview-a-nameproject-overviewa)
* [2. Entities <a name="entities"></a>](#2-entities-a-nameentitiesa)
* [3. Features <a name="features"></a>](#3-features-a-namefeaturesa)
* [4. Getting Started <a name="getting-started"></a>](#4-getting-started-a-namegetting-starteda)
* [5. Docker <a name="docker"></a>](#5-docker-a-namedockera)
* [6. Swagger <a name="swagger"></a>](#6-swagger-a-nameswaggera)
* [7. Contributing <a name="contributing"></a>](#7-contributing-a-namecontributinga)
* [8. License <a name="license"></a>](#8-license-a-namelicensea)

## 1. Project Overview <a name="project-overview"></a>

The BookShop project is a web-based REST application developed using Java Spring. It provides a platform for both shoppers and managers to interact with books, shopping carts, orders, and inventory. The project will be developed incrementally, with features broken down into manageable parts.

## 2. Entities <a name="entities"></a>

The following domain models (entities) are central to this application:

- **User**: Represents registered users with authentication details and personal information.
- **Role**: Defines user roles such as admin, manager or regular user.
- **Book**: Represents books available in the store.
- **Category**: Classifies books into different categories.
- **ShoppingCart**: Represents a user's shopping cart.
- **CartItem**: Represents an item in a user's shopping cart.
- **Order**: Represents an order placed by a user.
- **OrderItem**: Represents an item in a user's order.

## 3. Features <a name="features"></a>
### Shoppers Can:

- **Join and Sign In**: Users can register and sign in to browse and purchase books.
- **Browse Books**: Users can view all available books, explore individual book details.
- **Bookshelf Sections**: Users can explore bookshelf sections and view all books within a specific section.
- **Use the Basket**: Users can add books to their shopping basket, view its contents, and remove books from it.
- **Purchase Books**: Users can buy all the books in their basket and view their order history.

### Managers Can:

- **Manage Books**: Managers can add new books to the store, update book details, and remove books from the inventory.
- **Organize Sections**: Managers can create, modify, or delete bookshelf sections to organize the inventory.
- **Manage Receipts**: Managers can change the status of order receipts, such as marking them as “Shipped” or “Delivered”.

### Admins Can:
- **Manage Users**: Admins can create users with specific role

## 4. Getting Started <a name="getting-started"></a>

To get started with my Online Book Store project, follow these steps:

1. **Clone the Repository**: Clone this GitHub repository to your local machine.

2. **Install Dependencies**: Ensure you have Java Spring and necessary dependencies installed.

3. **Database Configuration**: Configure the database connection settings in the application properties.

4. **Run the Application**: Start the Spring application and access it through your web browser.

## 5. Docker <a name="docker"></a>

Project also offers a Docker containerized version of this application. To run it using Docker, follow these additional steps:

1. **Docker Setup**: Install Docker on your machine if not already installed.

2. **Add necessary dependency**: Add this lines at pom.xml in <dependencies> section:

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-docker-compose</artifactId>
        </dependency>

3. **Build Docker Image**: Build the Docker image using the provided Dockerfile.

4. **Run Docker Container**: Run the Docker container, ensuring it's connected to the database.

# 6. Swagger <a name="swagger"></a>

I have integrated Swagger for easy API documentation. To access the API documentation after running the application, visit the [Swagger API documentation](http://localhost:8080/swagger-ui.html).

# 7. Contributing <a name="contributing"></a>

We welcome contributions from the community! If you want to contribute to the BookShop project, please contact us via [email](mailto:vladyslavihnatiuk@gmail.com) or leave a suggestion on the [project page](https://github.com/Someboty/bookShop). 

# 8. License <a name="license"></a>

This project is licensed under the MIT License, which means you can use, modify, and distribute it for personal or commercial purposes.

Thank you for considering my Online BookShop project. I hope it serves as a valuable learning resource and proof of my skills for potential interviewers. Happy coding!
