FROM node:19.8.1 as builder
RUN mkdir /usr/src/app
WORKDIR /usr/src/app
ENV PATH /usr/src/app/node_modules/.bin:$PATH
EXPOSE 3000
COPY . /usr/src/app
RUN npm install
CMD ["npm", "start"]
