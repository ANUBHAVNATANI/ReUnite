import React from 'react';
import PersonOutlineIcon from '@material-ui/icons/PersonOutline';
import { Grid, Typography } from '@material-ui/core';
const Profile = () => {
  return (
    <div>
      <Grid container direction="row" justify="center">
        <Grid item>
          <PersonOutlineIcon style={{ fontSize: 100, margin: 15 }} />
        </Grid>
      </Grid>
      <Grid container direction="row" justify="center">
        <Grid item>
          <Typography>Mr. GoodWill</Typography>
        </Grid>
      </Grid>
    </div>
  );
};

export default Profile;
