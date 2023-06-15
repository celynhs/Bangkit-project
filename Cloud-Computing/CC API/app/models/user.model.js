module.exports = (sequelize, Sequelize) => {
    const User = sequelize.define("users", {
        email: {
            type: Sequelize.STRING
        },
        password: {
            type: Sequelize.STRING
        },
        name: {
            type: Sequelize.STRING
        },
        phone: {
            type: Sequelize.STRING
        },
        address: {
            type: Sequelize.TEXT('long')
        },
        city: {
            type: Sequelize.STRING
        },
        post_code: {
            type: Sequelize.STRING
        },
        budget: {
            type: Sequelize.BIGINT
        },
        user_type: {
            type: Sequelize.STRING
        },
    });

    return User;
};