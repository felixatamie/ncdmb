const fs = require("fs");
const path = require("path");
const csv = require('csv-parser');
/*

  ****** instructions *********
  1. call irecharge get discos endpoint to rerieve all their discos
  2. within the response of step 1, extract the bundles array and convert to it to a csv file
  3. name the file discos.csv
  4. run the following query on our mca db:
       select id, billername from mca1.billers where billername like 'MCA_OHM_NG_%';
  5. convert the response to a csv file named biller_irecharge.csv
  6. save files discos.csv and biller_irecharge.csv in thesame folder where this script is saved
  7. run the script "convert.js" as "node convert.js"

*/

const results = [];
const idAndBillerNameArray = convertId_BillernameFileToArrayOfJson();

fs.createReadStream('discos.csv')
  .pipe(csv())
  .on('data', (row) => {
    const code = row['code'];
    const description = row['description'];
    const minimumValue = row['minimum_value'];
    const maximumVaue = row['maximum_value']

    const billerId = getBillerIdfromArrayOfObjects(code);

    const insertSQL = `INSERT INTO bouquets (id, bouquet_name, amount, product_key, product_key2, product_key3, bouquet_category)
VALUES ( NULL, '${description}', ${minimumValue},'${code}', '${maximumVaue}', '${billerId}','IRECHARGE');`;

    results.push(insertSQL);
  })
  .on('end', () => {
    fs.writeFileSync('insert_irecharge_buoquets.sql', results.join('\n'));
    console.log('SQL insert statements generated in insert_irecharge_buoquets.sql');
  });

  


function convertId_BillernameFileToArrayOfJson()
{
  
  const csvFilePath = path.join(__dirname, "biller_irecharge.csv");
  const csvData = fs.readFileSync(csvFilePath, "utf-8");

  const rows = csvData.trim().split("\n");
  const headers = rows.shift().split(",");

  const result = rows.map(row => {
    const values = row.split(",");
    return headers.reduce((obj, header, i) => {
      obj[header.trim()] = values[i].trim();
      return obj;
    }, {});
  });

  return result;

}

function getBillerIdfromArrayOfObjects(discoCode)
{

  for( i = 0; i < idAndBillerNameArray.length; ++i)
  {
     const billerName = idAndBillerNameArray[i].billername.split("MCA_OHM_NG_")[1];
     if(discoCode === billerName)
     {
        return idAndBillerNameArray[i].id;
     }
  }

  return -999;
  
}



