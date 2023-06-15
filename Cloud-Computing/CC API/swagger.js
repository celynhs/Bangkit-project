const swaggerAutogen = require('swagger-autogen')()

const outputFile = './swagger_output.json'
const endpointsFiles = [
    './app/routes/auth.routes.js',
    './app/routes/bundle.routes.js',
    './app/routes/product.routes.js',
    './app/routes/transaction.routes.js',
]

swaggerAutogen(outputFile, endpointsFiles);