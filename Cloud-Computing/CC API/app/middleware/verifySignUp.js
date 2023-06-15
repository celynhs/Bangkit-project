/**
 * MIDDLEWARE: SEBUAH LAPISAN DARI APLIKASI BACKEND YANG BIASANYA DIGUNAKAN UNTUK
 *             MELAKUKAN PENGECEKAN SEPERTI PENGECEKAN TOKEN. JIKA MIDDLEWARE TIDAK
 *             BERHASIL DILEWATI, MAKA USER TIDAK AKAN BISA MENGGUNAKAN API.
 */

// MENGAMBIL MODELS USER
const db = require("../models");
const User = db.user;

// FUNCTION UNTUK MELAKUKAN PENGECEKAN APAKAH EMAIL SUDAH DIMILIKI
// OLEH USER LAIN KETIKA MELAKUKAN REGISTRASI.
checkDuplicateEmail = (req, res, next) => {
    // MENCARU USER BERDASARKAN EMAIL YANG DIBERIKAN
    User.findOne({
        where: {
            email: req.body.email
        }
    }).then(user => {
        // JIKA BERHASIL DITEMUKAN, MAKA MENGIRIMKAN STATUS CODE 400(BAD REQUEST)
        // DENGAN MESSAGE DIBAWAH.
        if (user) {
            res.status(400).send({
                message: "Failed! Email is already in use!"
            });
            return;
        }

        next();
    });
};

// MELAKUKAN EXPORTS TERHADAP FUNCTION verifySignUp AGAR DAPAT DIGUNAKAN DI FILE LAIN.
const verifySignUp = {
    checkDuplicateEmail: checkDuplicateEmail
};

module.exports = verifySignUp;