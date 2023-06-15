const { authJwt } = require("../middleware");
const controller = require("../controllers/product.controller");

module.exports = function (app) {
    app.use(function (req, res, next) {
        res.header(
            "Access-Control-Allow-Headers",
            "x-access-token, Origin, Content-Type, Accept"
        );
        next();
    });

    app.get(
        "/api/products",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.getAll
    );

    app.get(
        "/api/products/:productId",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.getById
    );

    app.post(
        "/api/products",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.createProduct
    );

    app.patch(
        "/api/products/:productId",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.editProduct
    );

    app.delete(
        "/api/products/:productId",
        [
            authJwt.verifyToken,
            authJwt.isAdmin,
        ],
        controller.deleteProduct
    );
};