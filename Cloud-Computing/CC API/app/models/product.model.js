module.exports = (sequelize, Sequelize) => {
    const Product = sequelize.define("products", {
        name: {
            type: Sequelize.STRING
        },
        actual_price: {
            type: Sequelize.STRING
        },
        discount_price: {
            type: Sequelize.STRING
        },
        ratings: {
            type: Sequelize.STRING
        },
        description: {
            type: Sequelize.TEXT('long')
        },
        image: {
            type: Sequelize.STRING
        },
    });

    return Product;
};