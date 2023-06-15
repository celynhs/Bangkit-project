module.exports = (sequelize, Sequelize) => {
    const TransactionDetail = sequelize.define("transaction_details", {
        transaction_id: {
            type: Sequelize.BIGINT,
            allowNull: false,
        },
        product_id: {
            type: Sequelize.BIGINT,
            allowNull: false,
        },
        price: {
            type: Sequelize.STRING,
            allowNull: false,
        },
        qty: {
            type: Sequelize.BIGINT,
            allowNull: false,
        },
        description: {
            type: Sequelize.TEXT('long'),
            allowNull: true,
        },
    });

    return TransactionDetail;
};