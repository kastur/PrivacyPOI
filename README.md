What is it
----------

We want to gather the locations for popular points of interest from
OpenStreetMap data, and categorize the POIs into "private" or "not private."

The idea is to have a location blocker service on mobile phones that can decide
given the category of current location, can decide whethe to share that location
with apps installed on the phone. For example, a person may not want to reveal
his religion to a random third-party app that wants to know his location. To
facilitate such a service, we track whether the location is categorized as a
"place of worship" according to OpenSteeetMap, and block location sharing at
such locations.

What is inside this repository
------------------------------
Right now, we are extracting POIs for the Los Angeles Metropolitan area, and
extracting the popular categories of POIs and their locations (in latitude,
longitude) format.

Here are some of the POI categories, and reasonable default "privacy" ratings
assigned to each:
`place_of_worship, 10, perturb or prevent
school, 3, share
fast_food, -3, share
restaurant, -4, share
fuel, 0, neutral
parking, 0, neutral
hospital, 10, perturb or prevent
library, 3, neutral
post_office, 0, neutral
cafe, -10, share
toilets, 15, perturb
bank, 5, perturb
post_box, 0, neutral
emergency_phone, 20, perturb
pharmacy, 10, perturb
telephone, 6, neutral
bench, 0, neutral
theatre, -5, share
townhall, 0, neutral
grave_yard, 10, perturb
swimming_pool, -3, share
atm, 0, perturb
bicycle_parking, 0, neutral
police, 10, perturb
bar, -10, share
waste_basket, 0, neutral
pub, -10, share
public_building, 0, neutral
cinema, -10, share
car_wash, 1, neutral
vending_machine, 0, neutral
waste_disposal, 0, neutral
recycling, 0, neutral
arts_centre, -5, share
snack_cart, 1, neutral
bbq, 0, neutral
doctors, 10, perturb or prevent
dentist, 10, perturb or prevent
car_rental, 3, neutral
gym, -2, share
bus_station, 3, neutral
courthouse, 10, perturb
ferry_terminal, 3, neutral
fuel;car_wash, 3, neutral
kindergarten, 1, neutral
nightclub, -5, share
prison, 10, perturb or prevent`

What does the privacy rating mean?
`
prevent = this is a place that the person is unlikely to want a corporation to know about: reveals the peron's religion, medical issues, or other sensitive personal situation. These places are frequented by the user, and sharing such habitual visits gives away the sensitive information. We should not let any third parties know about or predict the user's visit to this place.

perturb = this is a place that the person is unlikely to share with others, so we can perturb the true location. These places are not easily modeled by mobility models, and are sort of one-time or spontaneous visits. For example: the user visits a police station...this is probably not habitual.

Share = places that are often shared by users via social networks...location check-ins, geo-tagged photos. We should monitor and model the user sharing behavior in these categories of places and allow sharing of location.
`

Next Steps, Giant TODO:
-----------------------

1. Use these extracted locations to categorize the user location in real-time on a mobile phone (an Android device).
2. Use the OverrideFramework to "command" the LocationManager to stop sending updates to apps if the POI is "sensitive" or "private"
3. Have a UI that resembed IPtables, where a user can control through rules to specify which locations are senstiive to him/her.


