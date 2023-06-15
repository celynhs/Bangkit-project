const jwt = require("jsonwebtoken");

const config = require("../config/auth.config.js");

const db = require("../models");
const User = db.user;

verifyToken = (req, res, next) => {
    let token = req.headers["x-access-token"];

    if (!token) {
        return res.status(403).send({
            message: "No token provided!"
        });
    }

    jwt.verify(token, config.secret, (err, decoded) => {
        if (err) {
            return res.status(401).send({
                message: "Unauthorized!"
            });
        }
        req.userId = decoded.id;
        next();
    });
};

isAdmin = (req, res, next) => {
    let token = req.headers['x-access-token'];
    let { id } = jwt.decode(token);
    User.findOne({
        where: {
            id: id
        }
    }).then(user => {
        if (!user) {
            res.status(401).send({
                message: "User is not registered!"
            });
            return;
        } else {
            if (user.user_type != 'admin') {
                res.status(403).send({
                    message: "User is not an admin!"
                });
                return;
            }
        }

        next();
    });
};

isUser = (req, res, next) => {
    let token = req.headers['x-access-token'];
    let { id } = jwt.decode(token);
    User.findOne({
        where: {
            id: id
        }
    }).then(user => {
        if (!user) {
            res.status(401).send({
                message: "User is not registered!"
            });
            return;
        } else {
            if (user.user_type != 'user') {
                res.status(403).send({
                    message: "User is not an user!"
                });
                return;
            }
        }

        next();
    });
};

const authJwt = {
    verifyToken: verifyToken,
    isAdmin: isAdmin,
    isUser: isUser
};
module.exports = authJwt;