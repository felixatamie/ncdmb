// install dependencies first:
// npm install xlsx fs




const fs = require('fs');
const csv = require('csv-parser');

const results = [];

fs.createReadStream('bouquets_multichannel_staging.xml.csv')
  .pipe(csv())
  .on('data', (row) => {
    const bouquetName = row['bouquet_name'];
    const amount = row['amount'];
    const productKey = row['product_key'];
    const productKey2 = row['product_key2'] || null;
    const productKey3 = row['product_key3'] || null;
    const bouquetCategory = row['bouquet_category'];

    const insertSQL = `INSERT INTO bouquets (bouquet_name, amount, product_key, product_key2, product_key3, bouquet_category)
VALUES ('${bouquetName}', ${amount}, '${productKey}', ${productKey2 ? `'${productKey2}'` : 'NULL'}, ${productKey3 ? `'${productKey3}'` : 'NULL'}, '${bouquetCategory}');`;

    results.push(insertSQL);
  })
  .on('end', () => {
    fs.writeFileSync('insert_bouquets.sql', results.join('\n'));
    console.log('SQL insert statements generated in insert_bouquets.sql');
  });
