const axios = require('axios');

exports.getBundle = (req, res) => {
    const { budget } = req.body;
  axios
    .post('https://bebi-ml-service-3hcjvoufoq-et.a.run.app/recommendations', {
      budget
    })
    .then(response => {
      // Handle the response from the API
      res.send(response.data);
    })
    .catch(error => {
      // Handle any errors that occur during the request
      console.error(error);
      res.status(500).send('Error occurred while fetching recommendations.');
    });
};
