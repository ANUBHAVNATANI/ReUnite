import React, { Fragment, useState, useEffect } from 'react';
import PersonOutlineIcon from '@material-ui/icons/PersonOutline';
import { Box, Grid, Button, Typography } from '@material-ui/core';
import PublishIcon from '@material-ui/icons/Publish';
import Profile from './Profile';
import * as faceapi from 'face-api.js';

const MODEL_URL = '/models';
const UploadImage = () => {
  const [widget, setWidget] = useState(null);
  const [profileView, setProfileView] = useState(false);
  const [image, setImage] = useState('');
  useEffect(() => {
    setWidget(
      window.cloudinary.createUploadWidget(
        {
          cloudName: 'whiteknight',
          uploadPreset: 'b0z6jywd',
          sources: ['camera', 'url']
        },
        async (err, result) => {
          if (result.event === 'success') {
            console.log(result.info);

            await faceapi.loadSsdMobilenetv1Model(MODEL_URL);
            await faceapi.loadFaceLandmarkModel(MODEL_URL);
            await faceapi.loadFaceRecognitionModel(MODEL_URL);
            const img = await faceapi.fetchImage(result.info.secure_url);
            console.log(result.info.secure_url);
            // const img = await faceapi.fetchImage(
            //   'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80'
            // );

            const fullFaceDescription = await faceapi
              .detectSingleFace(img)
              .withFaceLandmarks()
              .withFaceDescriptor();

            if (!fullFaceDescription) {
              alert(`no human faces detected!`);
            } else {
              const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ im_url: result.info.secure_url })
              };
              fetch('https://relice.herokuapp.com/check', requestOptions)
                .then((response) => response.json())
                .then((resp) => {
                  console.log(resp);
                  alert('Reported');
                });
            }
          }
        }
      )
    );
  }, []);

  const handleOnIconClick = () => {
    console.log('hello');
    setProfileView(true);
  };

  const showWidget = () => {
    widget.open();
  };

  return (
    <Fragment>
      {profileView ? (
        <Profile />
      ) : (
        <div display="flex">
          <Grid container direction="row" justify="flex-end">
            <Grid item>
              <Button
                onClick={() => {
                  handleOnIconClick();
                }}
              >
                <PersonOutlineIcon style={{ fontSize: 50, margin: 15 }} />
              </Button>
            </Grid>
          </Grid>
          <Box
            justifyContent="center"
            display="flex"
            alignItems="center"
            style={{
              height: '75vh'
            }}
          >
            <Button variant="outlined" onClick={showWidget} color="primary">
              <PublishIcon color="primary" />
              <Typography>Upload Image</Typography>
            </Button>
          </Box>
        </div>
      )}
    </Fragment>
  );
};

export default UploadImage;
