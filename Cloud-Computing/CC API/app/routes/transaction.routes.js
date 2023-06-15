const { authJwt } = require("../middleware");
const controller = require("../controllers/transaction.controller");

module.exports = function (app) {
    app.use(function (req, res, next) {
        res.header(
            "Access-Control-Allow-Headers",
            "x-access-token, Origin, Content-Type, Accept"
        );
        next();
    });

    app.get(
        "/api/transactions",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.getAll
    );

    app.get(
        "/api/transactions/:transactionId",
        [
            authJwt.verifyToken,
        ],
        controller.getById
    );

    app.post(
        "/api/transactions",
        [
            authJwt.verifyToken,
            authJwt.isUser,
        ],
        controller.createTransaction
    );

    app.patch(
        "/api/transactions/:transactionId",
        [
            authJwt.verifyToken,
            authJwt.isUser,
        ],
        controller.editTransaction
    );

    app.delete(
        "/api/transactions/:transactionId",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.deleteTransaction
    );
};