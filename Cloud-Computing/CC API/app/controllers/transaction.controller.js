const db = require("../models");

const Transaction = db.transaction;
const TransactionDetail = db.transaction_detail;
const User = db.user;

const Op = db.Sequelize.Op;

exports.getAll = (req, res) => {
    Transaction.findAll({
        limit: parseInt(req.query.limit),
        offset: parseInt(req.query.offset)
    }).then(async (transactions) => {
        let data = [];
        for (let i = 0; i < transactions.length; i++) {
            let transDetailArr = [];
            let transactionDetails = await TransactionDetail.findAll({
                where: {
                    transaction_id: transactions[i].id
                }
            });
            await transactionDetails.every(transDetail => transDetailArr.push(transDetail));

            let obj = {
                user_id: transactions[i].user_id,
                location: transactions[i].location,
                subtotal: transactions[i].subtotal,
                bag_fee: transactions[i].bag_fee,
                tax: transactions[i].tax,
                total: transactions[i].total,
                transaction_details: transDetailArr,
            }
            data.push(obj);
        }
        res.send({ message: "Data fetched successfully!", data: data });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.getById = (req, res) => {
    Transaction.findOne({
        where: {
            id: req.params.transactionId
        }
    }).then(async (trans) => {
        let transDetailArr = [];
        let transactionDetails = await TransactionDetail.findAll({
            where: {
                transaction_id: trans.id
            }
        });
        await transactionDetails.every(transDetail => transDetailArr.push(transDetail));

        let obj = {
            user_id: trans.user_id,
            location: trans.location,
            subtotal: trans.subtotal,
            bag_fee: trans.bag_fee,
            tax: trans.tax,
            total: trans.total,
            transaction_details: transDetailArr,
        }
        res.send({ message: "Data fetched successfully!", data: obj });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.createTransaction = (req, res) => {
    Transaction.create({
        user_id: req.body.user_id,
        location: req.body.location,
        subtotal: req.body.subtotal,
        bag_fee: req.body.bag_fee,
        tax: req.body.tax,
        total: req.body.total,
        description: req.body.description,
    }).then(async (newTransaction) => {
        let transDetailArr = [];
        for (let i = 0; i < req.body.transaction_details.length; i++) {
            let dataDetail = await TransactionDetail.create({
                transaction_id: newTransaction.id,
                product_id: req.body.transaction_details[i].product_id,
                price: req.body.transaction_details[i].price,
                qty: req.body.transaction_details[i].qty,
                description: req.body.transaction_details[i].description,
            });

            transDetailArr.push(dataDetail);
        }

        User.findOne({
            where: {
                id: req.body.user_id,
            }
        }).then(async (user) => {
            user.update({
                budget: user.budget - req.body.total,
            })
        });

        let obj = {
            user_id: newTransaction.user_id,
            location: newTransaction.location,
            subtotal: newTransaction.subtotal,
            bag_fee: newTransaction.bag_fee,
            tax: newTransaction.tax,
            total: newTransaction.total,
            transaction_details: transDetailArr,
        }

        res.send({ message: "Transaction created successfully!", data: obj });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.editTransaction = (req, res) => {
    Transaction.findOne({
        where: {
            id: req.params.transactionId
        }
    }).then(async (trans) => {
        let currTotal = trans.total;
        User.findOne({
            where: {
                id: req.body.user_id,
            }
        }).then(async (user) => {
            let totalDiff = Number(currTotal) - Number(req.body.total);
            user.update({
                budget: Number(user.budget) - Number(totalDiff),
            })
        });

        trans.update({
            user_id: req.body.user_id,
            location: req.body.location,
            subtotal: req.body.subtotal,
            bag_fee: req.body.bag_fee,
            tax: req.body.tax,
            total: req.body.total,
            description: req.body.description,
        });

        await TransactionDetail.destroy({
            where: {
                transaction_id: req.params.transactionId,
            }
        });

        for (let i = 0; i < req.body.transaction_details.length; i++) {
            await TransactionDetail.create({
                transaction_id: req.params.transactionId,
                product_id: req.body.transaction_details[i].product_id,
                price: req.body.transaction_details[i].price,
                qty: req.body.transaction_details[i].qty,
                description: req.body.transaction_details[i].description,
            });
        }

        res.send({ message: "Data updated successfully!" });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}

exports.deleteTransaction = async (req, res) => {
    await TransactionDetail.destroy({
        where: {
            transaction_id: req.params.transactionId,
        }
    });

    Transaction.findOne({
        where: {
            id: req.params.transactionId,
        }
    }).then((trans) => {
        let currTotal = trans.total;
        User.findOne({
            where: {
                id: req.body.user_id,
            }
        }).then(async (user) => {
            user.update({
                budget: Number(user.budget) + Number(currTotal),
            })
        });
        trans.destroy();
        res.send({ message: "Data deleted successfully!" });
    }).catch(err => {
        res.status(500).send({ message: err.message });
    });
}