FROM python:bullseye

WORKDIR /code

COPY requirements.txt .

RUN pip install -r requirements.txt

COPY src/ .

EXPOSE 5000

# CMD [ "flask", "--app", "app.py", "run" ]
CMD ["flask", "run", "--host=0.0.0.0" "--debug"]
