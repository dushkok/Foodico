const functions = require("firebase-functions");
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/orders/{pushId}/')
    .onWrite((event: any) => {
        const token = event.after.val()["userToken"];
        const payload = {
            notification: {
                title: "Foodico order",
                body: "Your order is ready and it will be delivered in " +
                    (Math.floor(Math.random() * 50) + 10) + " minutes."
            }
        };

        setTimeout(() => {
            admin.messaging().sendToDevice(token, payload)
                .then((response: any) => {
                    console.log("Successfully sent notification:", response);
                })
                .catch((error: any) => {
                    console.log("Error sending notification:", error);
                });
        }, Math.floor(Math.random() * 15000) + 5000);
    });