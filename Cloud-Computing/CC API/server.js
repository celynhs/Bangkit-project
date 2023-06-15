const express = require("express");

const bodyParser = require("body-parser");

const cors = require("cors");

const app = express();

app.use(cors());

app.use(bodyParser.json());

app.use(bodyParser.urlencoded({ extended: true }));
const db = require("./app/models");
db.sequelize.sync({ force: false }).then(() => {
    console.log('Drop and Resync Db');
});

const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('./swagger_output.json');
app.use('/v1', swaggerUi.serve, swaggerUi.setup(swaggerDocument));

app.get("/", (req, res) => {
    res.json({ message: "Hi." });
});

require('./app/routes/auth.routes')(app);
require('./app/routes/product.routes')(app);
require('./app/routes/bundle.routes')(app);
require('./app/routes/transaction.routes')(app);

const port = parseInt(process.env.PORT) || 8080;

app.listen(port, () => {
    console.log("Server is running on port 8000.");
});
