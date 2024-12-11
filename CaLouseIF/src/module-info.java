module CaLouseIF {
	requires java.sql;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.base;
	opens main;
	opens model;
	opens connection;
	opens controller;
	opens view.admin;
	opens view.auth;
}