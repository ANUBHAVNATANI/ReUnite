import React from 'react';
import axios from 'axios';
import {
  Grid,
  Card,
  Select,
  MenuItem,
  Typography,
  InputLabel,
  TextField,
  FormControl,
  Button
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core';
import ComplaintReg from './ComplaintReg';

const useStyles = makeStyles((theme) => ({
  textField: {
    width: '45%',
    margin: 10,
    [theme.breakpoints.down('xs')]: {
      width: '90%'
    }
  },

  lowerTextField: {
    minWidth: 200,
    width: '22%',
    margin: 10,
    [theme.breakpoints.down('xs')]: {
      width: '40%',
      minWidth: 140
    }
  }
}));

const ComplaintForm = ({ Complaints, setComplaints }) => {
  const classes = useStyles();

  const [img, setImg] = React.useState('');
  const [name, setName] = React.useState('');
  const [gName, setGName] = React.useState('');
  const [gContact, setGContact] = React.useState('');
  const [lastSeen, setLastSeen] = React.useState('');
  const [missTime, setMissTime] = React.useState('');
  const [gender, setGender] = React.useState('');
  const [skin, setSkin] = React.useState('');
  const [height, setHeight] = React.useState('');
  const [age, setAge] = React.useState(0);

  const handleChange = (event, method) => {
    method(event.target.value);
  };

  const handleSubmit = (e) => {
    const data = {
      name,
      im_url:
        img ||
        'https://cdn.cdnparenting.com/articles/2018/12/Featured-image1.jpg',
      age: age,
      gender,
      skin,
      guardian: {
        name: gName,
        contact: gContact
      },
      missingTime: missTime,
      lastSeen
    };
    setComplaints([...Complaints, data]);
    console.log(data);
    axios
      .post('https://relice.herokuapp.com/store', data)
      .then((response) => {
        console.log(response);
        if (response.data.resp === 'CADDED') alert('Complaint Created');
        else alert('Complaint not Created, clear database');
      })
      .then(() => {
        setName('');
        setImg('');
        setGName('');
        setGContact('');
        setGContact('');
        setLastSeen('');
        setMissTime('');
        setGender('');
        setSkin('');
        setHeight('');
        setAge(0);
      })
      .catch((err) => {
        console.log(err);
        alert('Error');
      });
  };

  return (
    <Card style={{ padding: 10, borderRadius: '5%', margin: '2rem 0px' }}>
      <Typography align="center" variant="h5">
        Complaint Form
      </Typography>
      <Grid container style={{ marginTop: 20 }}>
        <Grid item display="flex">
          <TextField
            className={classes.textField}
            required
            id="outlined-required"
            label="Name"
            variant="outlined"
            value={name}
            onChange={(e) => handleChange(e, setName)}
          />

          <TextField
            className={classes.textField}
            required
            id="outlined-required"
            label="Parent/Guardian Name"
            variant="outlined"
            value={gName}
            onChange={(e) => handleChange(e, setGName)}
          />

          <TextField
            className={classes.textField}
            required
            id="outlined-required"
            label="Mobile No."
            variant="outlined"
            value={gContact}
            onChange={(e) => handleChange(e, setGContact)}
          />

          <TextField
            className={classes.textField}
            required
            id="outlined-required"
            label="Last Seen"
            variant="outlined"
            value={lastSeen}
            onChange={(e) => handleChange(e, setLastSeen)}
          />
        </Grid>

        <Grid container style={{ margin: 10 }}>
          <Grid item display="flex">
            <TextField
              id="datetime-local"
              label="Missing Time"
              type="datetime-local"
              InputLabelProps={{
                shrink: true
              }}
              value={missTime}
              onChange={(e) => handleChange(e, setMissTime)}
            />
          </Grid>
        </Grid>

        <Grid container display="flexbox">
          <Grid item>
            <TextField
              className={classes.lowerTextField}
              required
              id="outlined-required"
              label="Age"
              variant="outlined"
              value={age}
              onChange={(e) => handleChange(e, setAge)}
            />
            <FormControl variant="outlined" className={classes.lowerTextField}>
              <InputLabel>Gender</InputLabel>
              <Select
                labelId="demo-simple-select-outlined-label"
                id="demo-simple-select-outlined"
                value={gender}
                onChange={(e) => handleChange(e, setGender)}
                label="Gender"
              >
                <MenuItem value={'male'}>Male</MenuItem>
                <MenuItem value={'female'}>Female</MenuItem>
              </Select>
            </FormControl>
            <TextField
              className={classes.lowerTextField}
              required
              id="outlined-required"
              label="Skin"
              variant="outlined"
              value={skin}
              onChange={(e) => handleChange(e, setSkin)}
            />

            <TextField
              className={classes.lowerTextField}
              required
              id="outlined-required"
              label="Height"
              variant="outlined"
              value={height}
              onChange={(e) => handleChange(e, setHeight)}
            />
          </Grid>
          <Grid item align="center" style={{ margin: '2%' }}>
            <ComplaintReg img={img} setImg={setImg} />
            <Button color="primary" onClick={handleSubmit}>
              <Typography>Submit</Typography>
            </Button>
          </Grid>
        </Grid>
      </Grid>
    </Card>
  );
};

export default ComplaintForm;
