const { authJwt } = require("../middleware");
const controller = require("../controllers/bundle.controller");

module.exports = function (app) {
    app.use(function (req, res, next) {
        res.header(
            "Access-Control-Allow-Headers",
            "x-access-token, Origin, Content-Type, Accept"
        );
        next();
    });

    app.post(
        "/api/bundles",
        [
            authJwt.verifyToken,
            // authJwt.isUser,
        ],
        controller.getBundle
    );
};