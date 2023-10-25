[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](http://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/powered-by-responsibility.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/docker-container.svg)](https://forthebadge.com)

<h3>Welcome to the BookShop project!</h3> This README will guide you through the setup, structure, and functionality of my Java Spring-based online bookstore application. My aim is to create a seamless platform for users to browse, purchase, and manage books, while administrators maintain control over inventory and orders.


## Table of Contents
* [❓ Project Overview <a name="project-overview"></a>](#project-overview-a-nameproject-overviewa)
* [👨‍💻 Tech stack](#tech-stack)
* [📝 Entities <a name="entities"></a>](#entities-a-nameentitiesa)
* [🔧 Features <a name="features"></a>](#features-a-namefeaturesa)
* [⚡️ Getting Started <a name="getting-started"></a>](#getting-started-a-namegetting-starteda)
* [🎥 Video Presentation <a name="presentation"></a>](#video-presentation)
* [📮 Postman Collection <a name="postman"></a>](#postman-collection)
* [📚 Swagger <a name="swagger"></a>](#swagger-a-nameswaggera)
* [🔨 Contributing <a name="contributing"></a>](#contributing-a-namecontributinga)
* [💼 License <a name="license"></a>](#license-a-namelicensea)

## ❓Project Overview <a name="project-overview"></a>

The BookShop project is a web-based REST application developed using Java Spring. It provides a platform for both shoppers and managers to interact with books, shopping carts, orders, and inventory. The project will be developed incrementally, with features broken down into manageable parts.

## 👨‍💻Tech stack

Here's a brief high-level overview of the tech stack the **BookShop API** uses:

- [Spring Boot](https://spring.io/projects/spring-boot): provides a set of pre-built templates and conventions for creating stand-alone, production-grade Spring-based applications.
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html): provides features like authentication, authorization, and protection against common security threats.
- [Spring Web](https://spring.io/projects/spring-ws#overview): includes tools for handling HTTP requests, managing sessions, and processing web-related tasks.
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/): provides a higher-level abstraction for working with databases and includes support for JPA (Java Persistence API).
- [Hibernate](https://hibernate.org/): simplifies the interaction between Java applications and databases by mapping Java objects to database tables and vice versa.
- [Lombok](https://projectlombok.org/): helps reduce boilerplate code by automatically generating common code constructs (like getters, setters, constructors, etc.) during compile time.
- [Mapstruct](https://mapstruct.org/): generates mapping code based on annotations, reducing the need for manual, error-prone mapping code.
- [Liquibase](https://www.liquibase.org/): helps manage database schema changes over time, making it easier to track and deploy database updates.
- [Swagger](https://swagger.io/): provides a framework for generating interactive API documentation, allowing developers to understand, test, and use APIs more easily.
- [Docker](https://www.docker.com/): provides a consistent and reproducible way to deploy applications across different environments.

## 📝Entities <a name="entities"></a>

The following domain models (entities) are central to this application:

- **User**: Represents registered users with authentication details and personal information.
- **Role**: Defines user roles such as admin, manager or regular user.
- **Book**: Represents books available in the store.
- **Category**: Classifies books into different categories.
- **ShoppingCart**: Represents a user's shopping cart.
- **CartItem**: Represents an item in a user's shopping cart.
- **Order**: Represents an order placed by a user.
- **OrderItem**: Represents an item in a user's order.

##  🔧Features <a name="features"></a>
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
- **Manage Users**: Admins can create users with specific role and change users roles

## ⚡️Getting Started <a name="getting-started"></a>

First, let's download a [repository](https://github.com/Someboty/bookShop).

Via IDE:
- Open IntelliJ IDEA.
- Select "File" -> "New Project from Version Control."
- Paste the link: https://github.com/Someboty/bookShop.git

Via git console command:

*I'll use "d:\Projects" as example of project's location. You can replace it with another folder on your device*

```bash
cd d:\Projects
git clone https://github.com/Someboty/bookShop.git
cd d:\Projects\bookShop
.\mvnw.cmd clean package
```
Now we have only to launch app! We can use this command:

```bash
docker-compose up
```
That's all you need to know to start! 🎉

# 🎥Video Presentation

"Seeing is believing"? Sure, you can see a brief demonstration of my project by clicking on [this link](https://www.loom.com/share/b35ea76355f54be680dbcad82d3f6360). This way, you can get a firsthand look at how it works and the results it can achieve. Don't just take my word for it – experience it yourself!

# 📮Postman Collection

I've put together a handy Postman collection to simplify and speed up the process of testing my API and engaging with my application. To access the collection, just click on this [link](https://github.com/Someboty/bookShop/blob/master/src/main/resources/book_app.postman_collection.json). It's a convenient tool that will make your experience smoother and more efficient. Happy testing!

# 📚Swagger <a name="swagger"></a>

I have integrated Swagger for easy API documentation. To access the API documentation after running the application, visit the [Swagger API documentation](http://localhost:8080/swagger-ui.html).

# 🔨Contributing <a name="contributing"></a>

We welcome contributions from the community! If you want to contribute to the BookShop project, please contact us via [email](mailto:vladyslavihnatiuk@gmail.com) or leave a suggestion on the [project page](https://github.com/Someboty/bookShop). 

# 💼License <a name="license"></a>

This project is licensed under the MIT License, which means you can use, modify, and distribute it for personal or commercial purposes.

**Thank you for considering my Online BookShop project. I hope it serves as a valuable learning resource and proof of my skills for potential interviewers. Happy coding!**
