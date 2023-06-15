const db = require("../models");

const config = require("../config/auth.config");

const User = db.user;

const Op = db.Sequelize.Op;

var jwt = require("jsonwebtoken");
var bcrypt = require("bcryptjs");

exports.signup = (req, res) => {
    User.findOne({
        where: {
            email: req.body.email
        }
    }).then(user => {
        if (user) {
            return res.status(400).send({ message: "User with that username already exists!" });
        }

        User.create({
            email: req.body.email,
            password: bcrypt.hashSync(req.body.password, 8),
            name: req.body.name,
            phone: req.body.phone,
            address: req.body.address,
            city: req.body.city,
            post_code: req.body.post_code,
            budget: req.body.budget,
            user_type: req.body.user_type
        }).then(newUser => {
            res.send({ message: "User was registered successfully!" });
        }).catch(err => {
            res.status(500).send({ message: err.message });
        });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
};

exports.signin = (req, res) => {
    User.findOne({
        where: {
            email: req.body.email
        }
    }).then(user => {
        if (!user) {
            return res.status(404).send({ message: "User Not found." });
        }

        var passwordIsValid = bcrypt.compareSync(
            req.body.password,
            user.password
        );

        if (!passwordIsValid) {
            return res.status(401).send({
                accessToken: null,
                message: "Invalid Password!"
            });
        }

        var token = jwt.sign({ id: user.id }, config.secret, {
            expiresIn: 86400 // 24 hours
        });

        res.status(200).send({
            id: user.id,
            email: user.email,
            name: user.name,
            phone: user.phone,
            address: user.address,
            city: user.city,
            post_code: user.post_code,
            budget: user.budget,
            user_type: user.user_type,
            token: token,
        });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
};