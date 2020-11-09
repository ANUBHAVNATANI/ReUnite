import React, { useState, useEffect } from 'react';
import {
  Avatar,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Card,
  CardContent
} from '@material-ui/core';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { makeStyles } from '@material-ui/core/styles';
// TODO : remove mocking
import { Matches } from '../mockdata';

const useStyles = makeStyles((theme) => ({
  medium: {
    width: theme.spacing(10),
    height: theme.spacing(10),
    fontSize: '2rem',
    marginRight: '2rem'
  },
  large: {
    width: theme.spacing(12),
    height: theme.spacing(12),
    fontSize: '2rem',
    marginRight: '2rem'
  }
}));
const locations = ['Raja Park', 'Mansarovar', 'Sodala', 'City Palace'];

const MatchingProfiles = ({ profiles }) => {
  const classes = useStyles();
  console.log(profiles);
  const [selectedProfile, setProfile] = useState(0);
  return (
    <React.Fragment>
      <div style={{ display: 'flex', justifyContent: 'space-evenly' }}>
        {profiles.map((profile, index) => (
          <Avatar
            style={{
              cursor: 'pointer',
              border: `${index === selectedProfile ? '0.5rem solid red' : ''}`
            }}
            className={classes.medium}
            src={profile[0]}
            onClick={() => setProfile(index)}
          />
        ))}
      </div>

      <ul style={{ fontSize: '1.2rem' }}>
        {profiles[selectedProfile] && (
          <div>Accuracy : {(parseFloat(profiles[selectedProfile][1])*100).toFixed(2)}%</div>
        )}
        {profiles.length>0 && <div>Location : "{locations[Math.floor(10 * Math.random()) % 4]}"</div>}
      </ul>
    </React.Fragment>
  );
};

const MatchCard = ({ match }) => {
  //   const {
  //     name,
  //     age,
  //     gender,
  //     skin,
  //     im_url,
  //     guardian: { name: guardianName, contact: guardianContact },
  //     missingTime,
  //     lastSeen,
  //     matchingProfiles
  //   } = match;

  const [im_url] = match;
  //   const { name, gender, age, skin, lastSeen, ...matchingProfiles } = match2;
  const name = match[1][0];
  const gender = match[1][1];
  const age = match[1][2];
  const skin = match[1][3];
  const lastSeen = match[1][4];
  const matchingProfiles = match[1][5];

  console.log(name);
  const classes = useStyles();
  if(matchingProfiles.length > 0)
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
              {/* <div style={{ fontSize: '0.8rem' }}>
                Missing Time : {missingTime}
              </div> */}
              <div style={{ fontSize: '0.8rem' }}>Last Seen : {lastSeen}</div>
            </div>
            <div style={{ fontSize: '0.8rem', marginLeft: '2rem' }}>
              {/* <div>Guardian's Name : {guardianName}</div>
              <div>Guardian's Contact : {guardianContact}</div> */}
              <div>Age : {age}</div>
              <div>Gender : {gender}</div>
              <div>Skin : {skin}</div>
            </div>
          </div>
        </AccordionSummary>
        <AccordionDetails style={{ display: 'flex', flexDirection: 'column' }}>
          <MatchingProfiles profiles={matchingProfiles} />
        </AccordionDetails>
      </Accordion>
    </Card>
  );
  else return null;
};

const MatchedList = () => {
  const [matchState, setMatchState] = useState([]);

  useEffect(() => {
    const requestOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
      //body: JSON.stringify(data)
    };
    fetch('https://relice.herokuapp.com/getinfo', requestOptions)
      .then((response) => response.json())
      .then((resp) => {
        setMatchState(Object.entries(resp));
        console.log(Object.entries(resp));
      });
  }, []);

  return (
    <React.Fragment>
      {matchState.map((match, index) => (
        <MatchCard key={index} match={match} />
      ))}
    </React.Fragment>
  );
};

export default MatchedList;
