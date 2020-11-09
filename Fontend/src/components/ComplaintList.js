import React from 'react';
import {
  Avatar,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Card
} from '@material-ui/core';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { makeStyles } from '@material-ui/core/styles';
// TODO : remove mocking
// import { Complaints } from '../mockdata';

const useStyles = makeStyles((theme) => ({
  large: {
    width: theme.spacing(12),
    height: theme.spacing(12),
    fontSize: '2rem',
    marginRight: '2rem'
  }
}));

const ComplaintCard = ({ complaint }) => {
  const {
    name,
    age,
    gender,
    skin,
    im_url,
    guardian: { name: guardianName, contact: guardianContact },
    missingTime,
    lastSeen
  } = complaint;
  const classes = useStyles();
  return (
    <Card style={{ margin: '10px 0px' }}>
      <Accordion>
        <AccordionSummary
          expandIcon={<ExpandMoreIcon style={{ fontSize: '2rem' }} />}
        >
          <div style={{ width: '100%', display: 'flex', alignItems: 'center' }}>
            <Avatar className={classes.large} src={im_url} alt={name} />
            <div>
              <div style={{ fontSize: '2rem' }}>{name}</div>
              <div style={{ fontSize: '0.8rem' }}>
                Missing Time : {missingTime}
              </div>
              <div style={{ fontSize: '0.8rem' }}>Last Seen : {lastSeen}</div>
            </div>
          </div>
        </AccordionSummary>
        <AccordionDetails>
          <ul style={{ fontSize: '1.2rem' }}>
            <div>Guardian's Name : {guardianName}</div>
            <div>Guardian's Contact : {guardianContact}</div>
            <div>Age : {age}</div>
            <div>Gender : {gender}</div>
            <div>Skin : {skin}</div>
          </ul>
        </AccordionDetails>
      </Accordion>
    </Card>
  );
};

const ComplaintList = ({ Complaints }) => {
  return (
    <React.Fragment>
      <div style={{ fontSize: '2rem' }}>Complaints ({Complaints.length})</div>
      {Complaints.map((complaint, index) => (
        <ComplaintCard key={index} complaint={complaint} />
      ))}
    </React.Fragment>
  );
};

export default ComplaintList;
