/**
 * CONTROLLER: MERUPAKAN SEBUAH BAGIAN PADA APLIKASI BACKEND YANG DIPAKAI UNTUK MENYIMPAN LOGIC DARI SETIAP API.
 */


// MENGAMBIL MODEL DARI FOLDER MODELS
const db = require("../models");

// MENGAMBIL MODEL PRODUCT
const Product = db.product;

const Op = db.Sequelize.Op;

var jwt = require("jsonwebtoken");
var bcrypt = require("bcryptjs");

exports.getAll = (req, res) => {
    Product.findAll({
        limit: parseInt(req.query.limit),
        offset: parseInt(req.query.offset)
    }).then((products) => {
        res.send({ message: "Data fetched successfully!", data: products });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.getById = (req, res) => {
    Product.findOne({
        where: {
            id: req.params.productId
        }
    }).then((product) => {
        res.send({ message: "Data fetched successfully!", data: product });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.createProduct = (req, res) => {
    Product.create({
        name: req.body.name,
        actual_price: req.body.actual_price,
        discount_price: req.body.discount_price,
        ratings: req.body.ratings,
        description: req.body.description,
        average_rating: req.body.average_rating,
        image: req.body.image,
    }).then(newProduct => {
        res.send({ message: "Product created successfully!", data: newProduct });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.editProduct = (req, res) => {
    Product.findOne({
        where: {
            id: req.params.productId
        }
    }).then((product) => {
        product.update({
            name: req.body.name,
            actual_price: req.body.actual_price,
            discount_price: req.body.discount_price,
            ratings: req.body.ratings,
            description: req.body.description,
            average_rating: req.body.average_rating,
            image: req.body.image,
        })
        res.send({ message: "Data updated successfully!" });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.deleteProduct = (req, res) => {
    Product.findOne({
        where: {
            id: req.params.productId
        }
    }).then((product) => {
        product.destroy();
        res.send({ message: "Data deleted successfully!" });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}