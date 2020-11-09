import React, { useState, useEffect } from 'react';
import { Grid, Button, Box, Typography } from '@material-ui/core';
import PublishIcon from '@material-ui/icons/Publish';

const ComplaintReg = ({ img, setImg }) => {
  const [widget, setWidget] = useState(null);

  const showWidget = () => {
    widget.open();
  };

  const checkUploadResult = (resultEvent) => {
    if (resultEvent.event === 'success') {
      console.log(resultEvent.info);
      setImg(resultEvent.info.secure_url);
    }
  };

  useEffect(() => {
    setWidget(
      window.cloudinary.createUploadWidget(
        {
          cloudName: 'whiteknight',
          uploadPreset: 'b0z6jywd'
        },
        (err, result) => {
          checkUploadResult(result);
        }
      )
    );
  }, []);

  return (
    <div>
      <Grid container>
        <Grid item xs={4} style={{ margin: '2%' }}>
          <Grid item align="center" style={{ margin: '2%' }}>
            <Box
              justifyContent="center"
              display="flex"
              border={1}
              alignItems="center"
              variant="outlined"
              style={{ width: 200, height: 200, borderRadius: '5%' }}
            >
              {img ? (
                <img
                  src={img}
                  alt="user"
                  style={{ width: 200, height: 200, borderRadius: '5%' }}
                />
              ) : (
                <Button onClick={showWidget}>
                  <PublishIcon color="primary" />
                  <Typography>Upload Image</Typography>
                </Button>
              )}
            </Box>
          </Grid>

          {/* <Grid item align="center" style={{ margin: '2%' }}>
            <Button color="primary">
              <Typography>Submit</Typography>
            </Button>
          </Grid> */}
        </Grid>
      </Grid>
    </div>
  );
};

export default ComplaintReg;
