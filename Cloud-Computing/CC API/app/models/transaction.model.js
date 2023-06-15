module.exports = (sequelize, Sequelize) => {
    const Transaction = sequelize.define("transactions", {
        user_id: {
            type: Sequelize.BIGINT
        },
        location: {
            type: Sequelize.STRING
        },
        subtotal: {
            type: Sequelize.STRING
        },
        bag_fee: {
            type: Sequelize.STRING
        },
        tax: {
            type: Sequelize.STRING
        },
        total: {
            type: Sequelize.STRING
        },
    });

    return Transaction;
};