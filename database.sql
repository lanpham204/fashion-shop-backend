create table categories (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name nvarchar(100)
);
CREATE TABLE products (
    id INT NOT NULL AUTO_INCREMENT,
    name nvarchar(100) NOT NULL,
    thumbnail VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    description TEXT NOT NULL,
    cate_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (cate_id) REFERENCES categories(id)
);
create table sizes (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    size nvarchar(20)
);
create table size_products (
    product_id int,
    size_id int,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (size_id) REFERENCES sizes(id) ON DELETE CASCADE,
    primary key(product_id,size_id)
);
create table colors (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    color nvarchar(20)
);
create table color_products (
    product_id int,
    color_id int,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (color_id) REFERENCES colors(id) ON DELETE CASCADE,
    primary key(product_id,color_id)
);
create table product_images (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255),
    product_id int,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
create table roles (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20)
);
create table users (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email varchar(100),
    password varchar(100),
    fullname nvarchar(100),
    is_active boolean,
    role_id int,
     FOREIGN KEY (role_id) REFERENCES roles(id)
);
create table orders (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id int,
    fullname nvarchar(255),
    phone_number varchar(20),
    address nvarchar(255),
    note  nvarchar(255),
    total_money DECIMAL(10,2),
    order_date timestamp,
    status ENUM('UNPAID','PENDING', 'SHIPPING','DELIVERED', 'CANCELLED'),
    active boolean,
    payment_method nvarchar(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
create table order_details (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id int,
    product_id int,
    size nvarchar(20),
    color nvarchar(20),
    quantity int,
    price DECIMAL(10,2),
    total_money DECIMAL(10,2),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);



