// install dependencies first:
// npm install xlsx fs




const fs = require('fs');
const csv = require('csv-parser');

const results = [];

fs.createReadStream('Staff_List_Updated_Aug_2025.csv')
  .pipe(csv())
  .on('data', (row) => {
    const staffName = row['Full_Name'];
    console.log(staffName);
    const staffId = row['Employee_File_No'];
    const gradeLevel = row['Grade_Level'];

    const gradeLevelCadre = getCadre(gradeLevel);

    const insertSQL = `INSERT INTO ncdmbcanteemdb.ncdmbstaff (created_at, email,name, phone, staff_id, status, updated_at, cadre_id)
VALUES ('NOW()', 'NULL', '${staffName}', 'NULL','${staffId}', 'ACTIVE', 'NOW()','${gradeLevelCadre}');`;

    results.push(insertSQL);
  })
  .on('end', () => {
    fs.writeFileSync('insert_ncdmb_staff.sql', results.join('\n'));
    console.log('SQL insert statements generated in insert_ncdmb_staff.sql');
  });


  function getCadre(gradeLevel) {
      let cadre = null;

      // Normalize input (uppercase, trim spaces)
      const grade = gradeLevel.toUpperCase().trim();

      // Case 1: M1 - M6
      if (/^M([1-6])$/.test(grade)) {
        cadre = 1;
      } 
      // Case 2: SS1 - SS7
      else if (/^SS([1-7])$/.test(grade)) {
        cadre = 2;
      } 
      // Case 3: GL*
      else if (/^GL\d*/.test(grade)) {
        cadre = 2;
      }

      return cadre;
}
